package hu.mapro.model.generator;

import static com.google.common.base.Preconditions.checkState;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;

public class UpdateCodeWriter extends CodeWriter {

	PersistentUnitStore persistentStore;
	
	public UpdateCodeWriter(File target) {
		this(new FileStore(target));
	}
	
	public UpdateCodeWriter(PersistentUnitStore persistentStore) {
		super();
		this.persistentStore = persistentStore;
	}

	public interface Key {
		JPackage getPackage();
		String getFileName();
	}
	
	public interface Unit {

		Key getKey();
		
	}

	public interface ReadableUnit extends Unit {

		InputStream getInputStream() throws IOException;
		
	}
	
	public interface WritableUnit extends Unit {
		
		OutputStream getOutputStream() throws IOException;
		
	}
	
	
	public interface UnitStore {
		
		boolean exists(Key key) throws IOException;
		ReadableUnit get(Key key) throws IOException;
		WritableUnit create(Key key) throws IOException;
		
		Iterable<? extends ReadableUnit> getAll();
		
	}
	
	public interface PersistentUnitStore extends UnitStore {
		
		boolean contentEquals(Key key, ReadableUnit inputStream) throws IOException;
		WritableUnit overwrite(Key key) throws IOException;
		void delete(Key key) throws IOException;
		
	}
	

	final UnitStore cacheStore = new CacheStore();
	
	
	@Override
	public OutputStream openBinary(JPackage pkg, String fileName)
			throws IOException {
		return cacheStore.create(createKey(pkg, fileName)).getOutputStream();
	}

	@Override
	public void close() throws IOException {
		
		for (ReadableUnit p : persistentStore.getAll()) {
			if (!cacheStore.exists(p.getKey())) {
				persistentStore.delete(p.getKey());
			}
		}
		
		for (ReadableUnit c : cacheStore.getAll()) {
			if (persistentStore.exists(c.getKey())) {
				
				if (!persistentStore.contentEquals(c.getKey(), c)) {
					copy(c, persistentStore.overwrite(c.getKey()));
				}
				
			} else {
				copy(c, persistentStore.create(c.getKey()));
			}
		}
		
	}

	private void copy(ReadableUnit from, WritableUnit to)
			throws IOException {
		InputStream isFrom = from.getInputStream();
		OutputStream isTo = to.getOutputStream();
		try {
			IOUtils.copy(
					isFrom,
					isTo
			);
		} finally {
			if (isFrom!=null) isFrom.close();
			if (isTo!=null) isTo.close();
		}
	}
	

	static class KeyImpl implements Key {

		JPackage pkg;
		String fileName;
		
		public KeyImpl(JPackage pkg, String fileName) {
			super();
			this.pkg = pkg;
			this.fileName = fileName;
		}

		@Override
		public JPackage getPackage() {
			return pkg;
		}

		@Override
		public String getFileName() {
			return fileName;
		}
		
	}
	
	public static Key createKey(JPackage pkg, String fileName) {
		return new KeyImpl(pkg, fileName);
	}
	
	public static class CacheStore implements UnitStore {
		
		class CacheUnit implements ReadableUnit, WritableUnit {

			Key key;
			
			public CacheUnit(Key key) {
				super();
				this.key = key;
			}

			final ByteArrayOutputStream data = new ByteArrayOutputStream();
			
			@Override
			public Key getKey() {
				return key;
			}

			@Override
			public OutputStream getOutputStream() {
				return data;
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(data.toByteArray());
			}
			
		}
		
		Map<Object, CacheUnit> map = Maps.newHashMap();
		
		@Override
		public boolean exists(Key key) {
			return map.containsKey(mapKey(key));
		}

		@Override
		public ReadableUnit get(Key key) {
			checkState(exists(key), "does not exist: " + key);
			return map.get(mapKey(key));
		}

		@Override
		public WritableUnit create(Key key) {
			checkState(!exists(key), "alread exists: " + key);
			CacheUnit unit = new CacheUnit(key);
			map.put(mapKey(key), unit);
			return unit;
		}

		@Override
		public Iterable<? extends ReadableUnit> getAll() {
			return map.values();
		}
		
		
		String mapKey(Key key) {
			return key.getPackage().name()+"/"+key.getFileName();
		}
	}

	public static class FileStore implements PersistentUnitStore {

		File target;
		
		public FileStore(File target) {
			super();
			this.target = target;
		}

		final JCodeModel codeModel = new JCodeModel();
		
		@Override
		public boolean exists(Key key) throws IOException {
			return getFile(key).exists();
		}

		@Override
		public ReadableUnit get(final Key key) throws IOException {
			checkState(exists(key), "file does not exists: " + key);
			
			return new ReadableUnit() {
				
				@Override
				public Key getKey() {
					return key;
				}
				
				@Override
				public InputStream getInputStream() throws IOException {
					return new FileInputStream(getFile(key));
				}
			};
		}

		@Override
		public WritableUnit create(final Key key) throws IOException {
			checkState(!exists(key), "file already exists: " + key);
			
			return new WritableUnit() {
				
				@Override
				public Key getKey() {
					return key;
				}
				
				@Override
				public OutputStream getOutputStream() throws IOException {
					File file = getFile(key);
					file.getParentFile().mkdirs();
					return new FileOutputStream(file);
				}
			};
		}

		@Override
		public Iterable<? extends ReadableUnit> getAll() {
			return Iterables.transform(
					FileUtils.listFiles(target, null, true),
					new Function<File, ReadableUnit>() {
						@Override
						public ReadableUnit apply(final File input) {
							return new ReadableUnit() {
								
								@Override
								public Key getKey() {
									return new Key() {
										
										@Override
										public JPackage getPackage() {
											return FileStore.this.getPackage(input);
										}

										@Override
										public String getFileName() {
											return input.getName();
										}
									};
								}
								
								@Override
								public InputStream getInputStream() throws IOException {
									return new FileInputStream(input);
								}
							};
						}
					}
			);
		}

		@Override
		public WritableUnit overwrite(Key key) throws IOException {
			delete(key);
			return create(key);
		}

		@Override
		public void delete(Key key) throws IOException {
			checkState(exists(key), "file does not exists: " + key);
			
			File file = getFile(key);
			file.delete();
		}

		protected File getFile(Key key) throws IOException {
			return getFile(key.getPackage(), key.getFileName());
		}
		
	    protected File getFile(JPackage pkg, String fileName ) throws IOException {
	        File dir;
	        
	        if(pkg.isUnnamed())
	            dir = target;
	        else
	            dir = new File(target, toDirName(pkg));
	        
	        File fn = new File(dir,fileName);
	        
	        return fn;
	    }

		@Override
		public boolean contentEquals(Key key, ReadableUnit unit) throws IOException {
			File file = getFile(key);
			checkState(file.exists(), "file does not exist: " + file);
			
			InputStream is1 = unit.getInputStream();
			InputStream is2 = new FileInputStream(file);
			
			try {
				return IOUtils.contentEquals(is1, is2);
			} finally {
				if (is1!=null) is1.close();
				if (is2!=null) is2.close();
			}
		}	
		
		private JPackage getPackage(File file) {
			return getPackageDir(file.getParentFile());
		}
		
		private JPackage getPackageDir(File dir) {
			if (dir.equals(target)) {
				return codeModel.rootPackage();
			} else {
				return getPackageDir(dir.getParentFile()).subPackage(dir.getName());
			}
		}

	}

    /** Converts a package name to the directory name. */
    private static String toDirName( JPackage pkg ) {
        return pkg.name().replace('.',File.separatorChar);
    }
	
}

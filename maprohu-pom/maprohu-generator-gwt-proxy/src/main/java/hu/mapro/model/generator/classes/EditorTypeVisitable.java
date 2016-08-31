package hu.mapro.model.generator.classes;

import hu.mapro.meta.EditorType;

public abstract class EditorTypeVisitable<
	NONE,
	INLINE,
	FORM,
	PAGE
> {
	
	protected NONE getNone() {
		return null;
	}
	protected INLINE getInline() {
		return null;
	}
	protected FORM getForm() {
		return null;
	}
	protected PAGE getPage() {
		return null;
	}
	
	public interface VisitorInterface<
		NONE,
		INLINE,
		FORM,
		PAGE,
		T
	> {
		
		T none(NONE none);
		T inline(INLINE inline);
		T form(FORM form);
		T page(PAGE page);
		
	}

	
	public abstract class Visitor<T> implements VisitorInterface<NONE, INLINE, FORM, PAGE, T> {

		@Override
		public T none(NONE none) {
			return null;
		}

		@Override
		public T inline(INLINE inline) {
			return null;
		}

		@Override
		public T form(FORM form) {
			return null;
		}

		@Override
		public T page(PAGE page) {
			return null;
		}
		
	}
	
	public abstract class Visit extends Visitor<Void> {

		@Override
		final public Void none(NONE none) {
			visitNone(none);
			return null;
		}

		public void visitNone(NONE none) {
		}

		@Override
		final public Void inline(INLINE inline) {
			visitInline(inline);
			return null;
		}

		public void visitInline(INLINE inline) {
		}

		@Override
		final public Void form(FORM form) {
			visitForm(form);
			return null;
		}

		public void visitForm(FORM form) {
		}

		@Override
		final public Void page(PAGE page) {
			visitPage(page);
			return null;
		}

		public void visitPage(PAGE page) {
		}

		public Visit() {
			accept(this);
		}
		
	}
	
	public EditorTypeVisitable(EditorType editorType) {
		switch (editorType) {
		case INLINE:
			acceptor = new Acceptor() {
				final INLINE value = getInline();
				@Override
				<T> T accept(VisitorInterface<NONE, INLINE, FORM, PAGE, T> visitor) {
					return visitor.inline(value);
				}
			};
			break;
		case FORM:
			acceptor = new Acceptor() {
				final FORM value = getForm();
				@Override
				<T> T accept(VisitorInterface<NONE, INLINE, FORM, PAGE, T> visitor) {
					return visitor.form(value);
				}
			};
			break;
		case PAGE:
			acceptor = new Acceptor() {
				final PAGE value = getPage();
				@Override
				<T> T accept(VisitorInterface<NONE, INLINE, FORM, PAGE, T> visitor) {
					return visitor.page(value);
				}
			};
			break;
		default:
			acceptor = new Acceptor() {
				final NONE value = getNone();
				@Override
				<T> T accept(VisitorInterface<NONE, INLINE, FORM, PAGE, T> visitor) {
					return visitor.none(value);
				}
			};
			break;
		}
	}
	
	final Acceptor acceptor;
	
	final public <T> T accept(VisitorInterface<NONE, INLINE, FORM, PAGE, T> visitor) {
		return acceptor.accept(visitor);
	}
	

	abstract class Acceptor {
		abstract <T> T accept(VisitorInterface<NONE, INLINE, FORM, PAGE, T> visitor);
	}
	
	
}

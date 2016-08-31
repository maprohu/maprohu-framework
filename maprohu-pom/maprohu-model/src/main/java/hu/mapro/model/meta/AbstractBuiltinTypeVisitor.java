package hu.mapro.model.meta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class AbstractBuiltinTypeVisitor implements BuiltinTypeParamReturnsVisitor<Void, Void> {

	
	public void defaultVisit(BuiltinType<?> type) {
		throw new RuntimeException("not implemented");
	}
	
	@Override
	public final Void visitBigDecimal(BuiltinType<BigDecimal> type, Void param) {
		
		visitBigDecimal(type); return null;
	}

	@Override
	public final Void visitBigInteger(BuiltinType<BigInteger> type, Void param) {
		
		visitBigInteger(type); return null;
	}

	@Override
	public final Void visitBoolean(BuiltinType<Boolean> type, Void param) {
		
		visitBoolean(type); return null;
	}

	@Override
	public final Void visitByte(BuiltinType<Byte> type, Void param) {
		
		visitByte(type); return null;
	}

	@Override
	public final Void visitCharacter(BuiltinType<Character> type, Void param) {
		
		visitCharacter(type); return null;
	}

	@Override
	public final Void visitDate(BuiltinType<Date> type, Void param) {
		
		visitDate(type); return null;
	}

	@Override
	public final Void visitDouble(BuiltinType<Double> type, Void param) {
		
		visitDouble(type); return null;
	}

	@Override
	public final Void visitFloat(BuiltinType<Float> type, Void param) {
		
		visitFloat(type); return null;
	}

	@Override
	public final Void visitInteger(BuiltinType<Integer> type, Void param) {
		
		visitInteger(type); return null;
	}

	@Override
	public final Void visitLong(BuiltinType<Long> type, Void param) {
		
		visitLong(type); return null;
	}

	@Override
	public final Void visitShort(BuiltinType<Short> type, Void param) {
		
		visitShort(type); return null;
	}

	@Override
	public final Void visitString(BuiltinType<String> type, Void param) {
		
		visitString(type); return null;
	}

	@Override
	public final Void visitText(BuiltinType<String> type, Void param) {
		
		visitText(type); return null;
	}

	public void visitBigDecimal(BuiltinType<BigDecimal> type){defaultVisit(type);};
	public void visitBigInteger(BuiltinType<BigInteger> type){defaultVisit(type);};
	public void visitBoolean(BuiltinType<Boolean> type){defaultVisit(type);};
	public void visitByte(BuiltinType<Byte> type){defaultVisit(type);};
	public void visitCharacter(BuiltinType<Character> type){defaultVisit(type);};
	public void visitDate(BuiltinType<Date> type){defaultVisit(type);};
	public void visitDouble(BuiltinType<Double> type){defaultVisit(type);};
	public void visitFloat(BuiltinType<Float> type){defaultVisit(type);};
	public void visitInteger(BuiltinType<Integer> type){defaultVisit(type);};
	public void visitLong(BuiltinType<Long> type){defaultVisit(type);};
	public void visitShort(BuiltinType<Short> type){defaultVisit(type);};
	public void visitString(BuiltinType<String> type){defaultVisit(type);};
	public void visitText(BuiltinType<String> type){visitString(type);};
	
	
}

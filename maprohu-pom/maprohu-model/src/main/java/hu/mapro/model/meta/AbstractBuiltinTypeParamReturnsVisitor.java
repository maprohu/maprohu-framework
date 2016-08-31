package hu.mapro.model.meta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class AbstractBuiltinTypeParamReturnsVisitor<P, R> implements BuiltinTypeParamReturnsVisitor<P, R> {

	
	public R defaultVisit(BuiltinType<?> type, P param) {
		throw new RuntimeException("not implemented");
	}
	
	@Override
	public R visitBigDecimal(BuiltinType<BigDecimal> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitBigInteger(BuiltinType<BigInteger> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitBoolean(BuiltinType<Boolean> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitByte(BuiltinType<Byte> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitCharacter(BuiltinType<Character> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitDate(BuiltinType<Date> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitDouble(BuiltinType<Double> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitFloat(BuiltinType<Float> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitInteger(BuiltinType<Integer> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitLong(BuiltinType<Long> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitShort(BuiltinType<Short> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitString(BuiltinType<String> type, P param) {
		
		return defaultVisit(type, param);
	}

	@Override
	public R visitText(BuiltinType<String> type, P param) {
		
		return defaultVisit(type, param);
	}

}

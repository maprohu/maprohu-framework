package hu.mapro.model.meta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public interface BuiltinTypeParamReturnsVisitor<P, R> {
	
	R visitBigDecimal(BuiltinType<BigDecimal> type, P param);
	R visitBigInteger(BuiltinType<BigInteger> type, P param);
	R visitBoolean(BuiltinType<Boolean> type, P param);
	R visitByte(BuiltinType<Byte> type, P param);
	R visitCharacter(BuiltinType<Character> type, P param);
	R visitDate(BuiltinType<Date> type, P param);
	R visitDouble(BuiltinType<Double> type, P param);
	R visitFloat(BuiltinType<Float> type, P param);
	R visitInteger(BuiltinType<Integer> type, P param);
	R visitLong(BuiltinType<Long> type, P param);
	R visitShort(BuiltinType<Short> type, P param);
	R visitString(BuiltinType<String> type, P param);
	R visitText(BuiltinType<String> type, P param);

}

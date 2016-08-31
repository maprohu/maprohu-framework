package hu.mapro.model.impl;

public interface BuiltinTypeVisitorReturns<R> {

	R scalarBigDecimal();
	R scalarBigInteger();
	R scalarBoolean();
	R scalarByte();
	<T extends Enum<T>> R scalarEnum(Class<T> enumClass);
	R scalarCharacter();
	R scalarDate();
	R scalarDouble();
	R scalarFloat();
	R scalarInteger();
	R scalarLong();
	R scalarShort();
	R scalarString();

	R listBigDecimal();
	R listBigInteger();
	R listBoolean();
	R listByte();
	<T extends Enum<T>> R listEnum(Class<T> enumClass);
	R listCharacter();
	R listDate();
	R listDouble();
	R listFloat();
	R listInteger();
	R listLong();
	R listShort();
	R listString();
	
	R setBigDecimal();
	R setBigInteger();
	R setBoolean();
	R setByte();
	<T extends Enum<T>> R setEnum(Class<T> enumClass);
	R setCharacter();
	R setDate();
	R setDouble();
	R setFloat();
	R setInteger();
	R setLong();
	R setShort();
	R setString();
	
}

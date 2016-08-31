package hu.mapro.model.impl;

public interface BuiltinTypeFieldVisitor<R> {

	R scalarBigDecimal();
	R scalarBigInteger();
	R scalarBoolean();
	R scalarByte();
	R scalarCharacter();
	R scalarDate();
	R scalarDouble();
	R scalarFloat();
	R scalarInteger();
	R scalarLong();
	R scalarShort();
	R scalarString();
	R scalarLongId();

	R listBigDecimal();
	R listBigInteger();
	R listBoolean();
	R listByte();
	R listCharacter();
	R listDate();
	R listDouble();
	R listFloat();
	R listInteger();
	R listLong();
	R listShort();
	R listString();
	R listLongId();
	
	R setBigDecimal();
	R setBigInteger();
	R setBoolean();
	R setByte();
	R setCharacter();
	R setDate();
	R setDouble();
	R setFloat();
	R setInteger();
	R setLong();
	R setShort();
	R setString();
	R setLongId();
	
}

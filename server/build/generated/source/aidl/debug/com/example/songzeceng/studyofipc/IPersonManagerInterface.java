/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\songzeceng\\AndroidStudioProjects\\FirstJD\\server\\src\\main\\aidl\\com\\example\\songzeceng\\studyofipc\\IPersonManagerInterface.aidl
 */
package com.example.songzeceng.studyofipc;
// 即便是在同一包下，也要手动导入Person类
// Declare any non-default types here with import statements

public interface IPersonManagerInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.songzeceng.studyofipc.IPersonManagerInterface
{
private static final java.lang.String DESCRIPTOR = "com.example.songzeceng.studyofipc.IPersonManagerInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.songzeceng.studyofipc.IPersonManagerInterface interface,
 * generating a proxy if needed.
 */
public static com.example.songzeceng.studyofipc.IPersonManagerInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.songzeceng.studyofipc.IPersonManagerInterface))) {
return ((com.example.songzeceng.studyofipc.IPersonManagerInterface)iin);
}
return new com.example.songzeceng.studyofipc.IPersonManagerInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getPeople:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.example.songzeceng.studyofipc.Person> _result = this.getPeople();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_addPerson:
{
data.enforceInterface(DESCRIPTOR);
com.example.songzeceng.studyofipc.Person _arg0;
if ((0!=data.readInt())) {
_arg0 = com.example.songzeceng.studyofipc.Person.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.addPerson(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_updatePerson:
{
data.enforceInterface(DESCRIPTOR);
com.example.songzeceng.studyofipc.Person _arg0;
_arg0 = new com.example.songzeceng.studyofipc.Person();
com.example.songzeceng.studyofipc.Person _result = this.updatePerson(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_updatePerson2:
{
data.enforceInterface(DESCRIPTOR);
com.example.songzeceng.studyofipc.Person _arg0;
if ((0!=data.readInt())) {
_arg0 = com.example.songzeceng.studyofipc.Person.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
com.example.songzeceng.studyofipc.Person _result = this.updatePerson2(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.songzeceng.studyofipc.IPersonManagerInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.util.List<com.example.songzeceng.studyofipc.Person> getPeople() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.example.songzeceng.studyofipc.Person> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPeople, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.example.songzeceng.studyofipc.Person.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addPerson(com.example.songzeceng.studyofipc.Person person) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((person!=null)) {
_data.writeInt(1);
person.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addPerson, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.example.songzeceng.studyofipc.Person updatePerson(com.example.songzeceng.studyofipc.Person person) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.example.songzeceng.studyofipc.Person _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updatePerson, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.example.songzeceng.studyofipc.Person.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
if ((0!=_reply.readInt())) {
person.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.example.songzeceng.studyofipc.Person updatePerson2(com.example.songzeceng.studyofipc.Person person) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.example.songzeceng.studyofipc.Person _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((person!=null)) {
_data.writeInt(1);
person.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updatePerson2, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.example.songzeceng.studyofipc.Person.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
if ((0!=_reply.readInt())) {
person.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getPeople = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addPerson = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updatePerson = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_updatePerson2 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public java.util.List<com.example.songzeceng.studyofipc.Person> getPeople() throws android.os.RemoteException;
public void addPerson(com.example.songzeceng.studyofipc.Person person) throws android.os.RemoteException;
public com.example.songzeceng.studyofipc.Person updatePerson(com.example.songzeceng.studyofipc.Person person) throws android.os.RemoteException;
public com.example.songzeceng.studyofipc.Person updatePerson2(com.example.songzeceng.studyofipc.Person person) throws android.os.RemoteException;
}

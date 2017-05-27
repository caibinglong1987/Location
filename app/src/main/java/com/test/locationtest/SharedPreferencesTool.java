package com.test.locationtest;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesTool {

	public static final String STRINGTYPE = "String";
	public static final String INTTYPE = "Integer";
	public static final String LONGTYPE = "Long";
	public static final String BOOLEANTYPE = "Boolean";

	public static void clearPreferences(Context ctx,String prefName){
		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = properties.edit();
		editor.clear();
	}
	
	public static void putValue(Context ctx,String prefName,String key,Object value){
		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = properties.edit();
		
		String typeName = value.getClass().getSimpleName();
		if(INTTYPE.equals(typeName)){
			editor.putInt(key, (Integer) value);
		}
		if(BOOLEANTYPE.equals(typeName)){
			editor.putBoolean(key, (Boolean) value);
		}
		if(STRINGTYPE.equals(typeName)){
			editor.putString(key, (String) value);
		}
		if(LONGTYPE.equals(typeName)){
			editor.putLong(key, (Long) value);
		}
		else{
			throw new RuntimeException();
		}
		editor.apply();
	}

	public static int getIntValue(Context ctx,String prefName,String key){
		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return properties.getInt(key, -1);
	}

	public static String getStringValue(Context ctx,String prefName,String key){
		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return properties.getString(key,null);
	}

	public static boolean getBooleanValue(Context ctx,String prefName,String key){
		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return properties.getBoolean(key, false);
	}

	public static long getLongValue(Context ctx,String prefName,String key){
		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return properties.getLong(key, -1);

	}
	
//	public static Object getValue(Context ctx,String prefName,String key,String type){
//		SharedPreferences properties =ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
//		Object obj = null;
//		if(INTTYPE.equals(type)){
//			obj = properties.getInt(key, -1);
//		}
//		if(BOOLEANTYPE.equals(type)){
//			obj = properties.getBoolean(key, false);
//		}
//		if(STRINGTYPE.equals(type)){
//			obj = properties.getString(key, null);
//		}
//		if(LONGTYPE.equals(type)){
//			obj = properties.getLong(key, -1);
//		}
//		return obj;
//	}
}

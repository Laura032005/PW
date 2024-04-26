package com.spring.loginroles.Messages;

public class Message {

    //Errores
    public static final String MENSAJE_ERROR_LOGIN = "Credenciales invalidas";
    public static final String MENSAJE_ERROR_EMAIL = "El correo electrónico ya existe, intenta con otro";
    public static final String MENSAJE_ERROR_ROL = "El rol %s no existe";
    public static final String MENSAJE_ERROR_CIUDAD = "La ciudad no existe";
    public static final String MENSAJE_ERROR_EXCEDIDO = "Hay suficientes %s en esa ciudad";
    public static final String MENSAJE_ERROR_ID_USUARIO = "No se encontró un usuario con ese Id";
    public static final String MENSAJE_ERROR_ID_ROL = "No se encontró un rol con ese ID:";
    public static final String MENSAJE_ERROR_PERMISO = "No tienes permisos para actualizar este usuario";
    public static final String MENSAJE_ERROR_AUTOIZADO = "Solo los %s pueden crear un %s";
    public static final String MENSAJE_ERROR_ELIMINAR = "No tienes permisos para eliminar este usuario";

    //Exitosos
    public static final String MENSAJE_EXITOSO_REGISTRO = "Registro de %s exitoso";
    public static final String MENSAJE_EXITOSO_ACTUALIZADO = "Usuario actualizado exitosamente";
    public static final String MENSAJE_ERROR_ELIMINACION = "No puede eliminar este usuario";
    public static final String MENSAJE_EXITOSO_ELIMINADO = "Usuario eliminado exitosamente";

}

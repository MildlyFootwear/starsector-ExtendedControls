package Shoey.ExtendedControls.Kotlin

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.ui.UIComponentAPI
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

class EasyReflect {

    private var log = Global.getLogger(this.javaClass)

    //Required to execute obfuscated methods without referencing their obfuscated class name.
    fun invokeMethod(methodName: String, instance: Any, vararg arguments: Any?): Any? {
        lateinit var method: Any

        val clazz = instance.javaClass
        val args = arguments.map { it!!::class.javaPrimitiveType ?: it::class.java }
        val methodType = MethodType.methodType(Void.TYPE, args)

        method = clazz.getMethod(methodName, *methodType.parameterArray())
        try {
            return invokeMethodHandle.invoke(method, instance, arguments)
        } catch (e: Exception) {
            return null
        }
    }

    fun getChildrenCopy(instance: Any): List<UIComponentAPI?> {
        return invokeMethod("getChildrenCopy", instance) as List<UIComponentAPI?>
    }

    //Used to be able to find specific files without having to reference their obfuscated class name.
    fun hasMethodOfName(name: String, instance: Any): Boolean {
        val instancesOfMethods: Array<out Any> = instance.javaClass.getDeclaredMethods()
        return instancesOfMethods.any { getMethodNameHandle.invoke(it) == name }
    }

    private val fieldClass = Class.forName("java.lang.reflect.Field", false, Class::class.java.classLoader)
    private val setFieldHandle = MethodHandles.lookup()
        .findVirtual(fieldClass, "set", MethodType.methodType(Void.TYPE, Any::class.java, Any::class.java))
    private val setFieldAccessibleHandle = MethodHandles.lookup()
        .findVirtual(fieldClass, "setAccessible", MethodType.methodType(Void.TYPE, Boolean::class.javaPrimitiveType))

    val methodClass = Class.forName("java.lang.reflect.Method", false, Class::class.java.classLoader)
    val getMethodNameHandle =
        MethodHandles.lookup().findVirtual(methodClass, "getName", MethodType.methodType(String::class.java))
    val invokeMethodHandle = MethodHandles.lookup().findVirtual(
        methodClass,
        "invoke",
        MethodType.methodType(Any::class.java, Any::class.java, Array<Any>::class.java))

}
package io.gianluigip.spectacle.common.utils

import java.util.Collections

/**
 * Hack from https://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
 */
fun setEnv(newenv: Map<String, String>?) {
    try {
        val processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment")
        val theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment")
        theEnvironmentField.isAccessible = true

        val env = theEnvironmentField.get(null) as MutableMap<String, String>
        env.putAll(newenv!!)

        val theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment")
        theCaseInsensitiveEnvironmentField.setAccessible(true)
        val cienv = theCaseInsensitiveEnvironmentField.get(null) as MutableMap<String, String>
        cienv.putAll(newenv)
    } catch (e: NoSuchFieldException) {
        val classes = Collections::class.java.declaredClasses
        val env = System.getenv()
        for (cl in classes) {
            if ("java.util.Collections\$UnmodifiableMap" == cl.name) {
                val field = cl.getDeclaredField("m")
                field.setAccessible(true)
                val obj: Any = field.get(env)
                val map = obj as MutableMap<String, String>
                map.clear()
                map.putAll(newenv!!)
            }
        }
    }
}
package io.gianluigip.spectacle.common.components

import mui.material.GridProps
import mui.material.TextFieldProps
import muix.pickers.DateTimePickerProps
import react.ReactNode

// TODO: Remove when it will be implemented in wrappers
/**
 * Grid
 */
inline var GridProps.xs: Int
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic().xs = value
    }
inline var GridProps.md: Int
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic().md = value
    }
inline var GridProps.xl: Int
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic().xl = value
    }

/**
 * DateTimePicker
 */
external interface DateTimeFns {
    fun toISOString(): String
}

inline var DateTimePickerProps.label: String
    get() = TODO("Prop is write-only!")
    set(crossinline value) {
        asDynamic().label = value
    }

inline var DateTimePickerProps.value: DateTimeFns?
    get() = TODO("Prop is write-only!")
    set(crossinline value) {
        asDynamic().value = value
    }

inline var DateTimePickerProps.renderInput: (TextFieldProps) -> ReactNode
    get() = TODO("Prop is write-only!")
    set(crossinline value) {
        asDynamic().renderInput = { props: TextFieldProps -> value(props) }
    }


inline var DateTimePickerProps.onChange: (DateTimeFns?) -> Unit
    get() = TODO("Prop is write-only!")
    set(crossinline value) {
        asDynamic().onChange = { changed: DateTimeFns? -> value(changed) }
    }

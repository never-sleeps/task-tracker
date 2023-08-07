package me.neversleeps.common.statemachine

enum class ObjectState {
    NONE, // не инициализировано состояние

    NEW, // новый (только созданный)
    ADULT, // взрослый
    OLD, // старый

    ERROR, // ошибки вычисления
}

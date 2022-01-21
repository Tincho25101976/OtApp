package com.vsg.helper.common.operation

enum class DBOperation(val data: String, val value: Int) {
    INSERT("Nuevo...", 1),
    UPDATE("Actualizar...", 2),
    DELETE("Eliminar...", 3),
    LIST("Listado...", 4),
    VIEW("Visualizar...", 5),
    INDEFINIDO("Indefinido", -1)
}

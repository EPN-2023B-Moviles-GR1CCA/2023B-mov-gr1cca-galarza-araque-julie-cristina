package com.example.examen01

class BDD {

    companion object {

        var bddAplicacion: AutorLibroFirestore? = null
            get() {
                if (field == null) {
                    field = AutorLibroFirestore()
                }
                return field
            }
    }
}
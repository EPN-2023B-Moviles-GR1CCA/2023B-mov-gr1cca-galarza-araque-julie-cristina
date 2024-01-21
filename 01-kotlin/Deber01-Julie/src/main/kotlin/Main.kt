import Model.Autor
import Model.Libro
import Repositorio.AutorRepository
import Repositorio.LibroRepository
import java.text.SimpleDateFormat
import java.util.*

fun main( ){
0



    val autorRepo = AutorRepository("src/datos/autores.json")
    val libroRepo = LibroRepository("src/datos/libros.json")

    var continuar = true
    while (continuar) {
        println("Seleccione una opción:")
        println("1. Administrar Autores")
        println("2. Administrar Libros")
        println("3. Salir")

        when (readLine()?.toIntOrNull() ?: 0) {
            1 -> menuAutores(autorRepo)
            2 -> menuLibros(libroRepo, autorRepo)
            3 -> continuar = false
            else -> println("Opción no válida. Por favor, elige un número del menú.")
        }
    }
}

fun menuAutores(autorRepo: AutorRepository) {
    var continuar = true
    while (continuar) {
        println("Seleccione una opción para Autores:")
        println("1. Crear Autor")
        println("2. Mostrar Autores")
        println("3. Actualizar Autor")
        println("4. Eliminar Autor")
        println("5. Volver al menú principal")

        when (readLine()?.toIntOrNull() ?: 0) {
            1 -> {
                // Lógica para crear un nuevo autor

                var idAutor: Int
                    do {
                    println("Ingrese el ID del autor:")
                    idAutor = readLine()?.toIntOrNull() ?: -1

                    if (!autorRepo.isIdUnique(idAutor)) {
                        println("El ID ingresado ya está en uso. Por favor, ingrese un ID único.")
                    }
                } while (!autorRepo.isIdUnique(idAutor))
                println("Ingrese el nombre del autor:")
                val nombre = readLine() ?: ""
                println("Ingrese la fecha de nacimiento (formato yyyy-MM-dd):")
                val fechaNacimientoStr = readLine() ?: ""
                val fechaNacimiento = SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimientoStr)
                println("Ingrese la nacionalidad del autor:")
                val nacionalidad = readLine() ?: ""
                println("¿El autor tiene premio Nobel? (true/false):")
                val premioNobel = readLine()?.toBoolean() ?: false

                // Crear un nuevo objeto Autor con los datos proporcionados (incluyendo el ID)
                val nuevoAutor = Autor(idAutor, nombre, fechaNacimiento, nacionalidad, premioNobel)

                // Llamar al repositorio para crear el nuevo autor
                val autorCreado = autorRepo.create(nuevoAutor)
                println("Autor creado: $autorCreado")

            }
            2 -> {
                // Lógica para mostrar todos los autores
                // val autores = autorRepo.getAutores()
                // Imprimir los autores obtenidos
                // Obtener la lista de autores del repositorio
                val autores = autorRepo.getAutores()
                println("Lista de Autores:")
                autores.forEach { println(it) }

            }
            3 -> {
                // Lógica para actualizar un autor existente

                println("Ingrese el ID del autor a actualizar:")
                val idAutor = readLine()?.toIntOrNull() ?: -1

                // Verificar si el autor existe
                val autorExistente = autorRepo.getAutorByIdentificador(idAutor)
                if (autorExistente != null) {
                    println("Ingrese el nuevo nombre del autor:")
                    val nuevoNombre = readLine() ?: ""
                    println("Ingrese la nueva fecha de nacimiento (formato yyyy-MM-dd):")
                    val nuevaFechaNacimientoStr = readLine() ?: ""
                    val nuevaFechaNacimiento = SimpleDateFormat("yyyy-MM-dd").parse(nuevaFechaNacimientoStr)
                    println("Ingrese la nueva nacionalidad del autor:")
                    val nuevaNacionalidad = readLine() ?: ""
                    println("¿El autor tiene premio Nobel? (true/false):")
                    val nuevoPremioNobel = readLine()?.toBoolean() ?: false

                    // Crear un nuevo objeto Autor con los datos actualizados (incluyendo el ID)
                    val autorActualizado = Autor(idAutor, nuevoNombre, nuevaFechaNacimiento, nuevaNacionalidad, nuevoPremioNobel)

                    // Llamar al repositorio para actualizar el autor
                    val autorModificado = autorRepo.updateByIdentificador(idAutor, autorActualizado)
                    if (autorModificado != null) {
                        println("Autor actualizado: $autorModificado")
                    } else {
                        println("No se pudo actualizar el autor.")
                    }
                } else {
                    println("El autor con ID $idAutor no existe.")
                }


            }
            4 -> {
                // Lógica para eliminar un autor
                
                println("Ingrese el ID del autor a eliminar:")
                val idAutorEliminar = readLine()?.toIntOrNull() ?: -1

                // Verificar si el autor existe
                val eliminado = autorRepo.deleteById(idAutorEliminar)
                if (eliminado) {
                    println("Autor con ID $idAutorEliminar eliminado correctamente.")
                } else {
                    println("El autor con ID $idAutorEliminar no existe.")
                }

            }
            5 -> continuar = false
            else -> println("Opción no válida. Por favor, elige un número del menú.")
        }
    }
}
fun menuLibros(libroRepo: LibroRepository, autorRepo: AutorRepository) {
    var continuar = true
    while (continuar) {
        println("Seleccione una opción para Libros:")
        println("1. Crear Libro")
        println("2. Mostrar Libros de un Autor")
        println("3. Actualizar Libro")
        println("4. Eliminar Libro")
        println("5. Volver al menú principal")

        when (readLine()?.toIntOrNull() ?: 0) {
            1 -> {
                // Lógica para crear un nuevo libro
                println("Creación de un nuevo libro:")
                var idLibro: Int
                var idAutor: Int

                // Solicitar el ID del libro y verificar su unicidad
                do {
                    println("Ingrese el ID del libro:")
                    idLibro = readLine()?.toIntOrNull() ?: -1

                    if (!libroRepo.isBookIdUnique(idLibro)) {
                        println("El ID ingresado ya está en uso. Por favor, ingrese un ID único.")
                    }
                } while (!libroRepo.isBookIdUnique(idLibro))

                // Solicitar el ID del autor y verificar si existe
                do {
                    println("Ingrese el ID del autor del libro:")
                    idAutor = readLine()?.toIntOrNull() ?: -1

                    val autorExistente = autorRepo.getAutorByIdentificador(idAutor)
                    if (autorExistente == null) {
                        println("El autor con ID $idAutor no existe. Ingrese un ID válido.")
                    }
                } while (autorExistente == null)

                println("Ingrese el título del libro:")
                val titulo = readLine() ?: ""

                println("Ingrese la fecha de publicación (formato yyyy-MM-dd):")
                val fechaPublicacionStr = readLine() ?: ""
                val fechaPublicacion = SimpleDateFormat("yyyy-MM-dd").parse(fechaPublicacionStr)

                println("Ingrese el género del libro:")
                val genero = readLine() ?: ""

                println("Ingrese el precio del libro:")
                val precio = readLine()?.toDoubleOrNull() ?: 0.0

                println("¿Es un best seller? (true/false):")
                val bestSeller = readLine()?.toBoolean() ?: false

                // Crear un nuevo objeto Libro con los datos ingresados y el ID único
                val nuevoLibro = Libro(idLibro, idAutor, titulo, fechaPublicacion, genero, precio, bestSeller)

                // Llamar al repositorio para crear el nuevo libro
                val libroCreado = libroRepo.create(nuevoLibro)
                println("Libro creado: $libroCreado")
            }
            2 -> {
                // Lógica para mostrar los libros de un autor
                // Solicitar el ID del autor y obtener los libros asociados a ese autor: libroRepo.getBooksByAutor(idAutor)
                // Imprimir los libros obtenidos
                println("Ingrese el ID del autor para mostrar sus libros:")
                val idAutor = readLine()?.toIntOrNull() ?: -1

                // Obtener los libros asociados al ID del autor proporcionado
                val libros = libroRepo.getBooksByAutor(idAutor)

                // Mostrar los libros obtenidos
                println("Lista de libros del autor:")
                libros.forEach { println(it) }
            }
            3 -> {
                // Lógica para actualizar un libro existente
                println("Ingrese el ID del autor del libro a actualizar:")
                val idAutor = readLine()?.toIntOrNull() ?: -1

                val autorExistente = autorRepo.getAutorByIdentificador(idAutor)
                if (autorExistente != null) {
                    println("Ingrese el ID del libro a actualizar:")
                    val idLibro = readLine()?.toIntOrNull() ?: -1

                    val libroExistente = libroRepo.getBookByIdentificador(idLibro)
                    if (libroExistente != null) {
                        println("Ingrese el nuevo título del libro:")
                        val nuevoTitulo = readLine() ?: libroExistente.titulo

                        println("Ingrese la nueva fecha de publicación (formato yyyy-MM-dd):")
                        val nuevaFechaPublicacionStr = readLine() ?: SimpleDateFormat("yyyy-MM-dd").format(libroExistente.fechaPublicacion)
                        val nuevaFechaPublicacion = SimpleDateFormat("yyyy-MM-dd").parse(nuevaFechaPublicacionStr)

                        println("Ingrese el nuevo género del libro:")
                        val nuevoGenero = readLine() ?: libroExistente.genero

                        println("Ingrese el nuevo precio del libro:")
                        val nuevoPrecio = readLine()?.toDoubleOrNull() ?: libroExistente.precio

                        println("¿Es un best seller? (true/false):")
                        val nuevoBestSeller = readLine()?.toBoolean() ?: libroExistente.bestSeller

                        val libroActualizado = Libro(idLibro, idAutor, nuevoTitulo, nuevaFechaPublicacion, nuevoGenero, nuevoPrecio, nuevoBestSeller)

                        val libroModificado = libroRepo.updateByIdentificadorAndIdAutor(idLibro, libroActualizado, idAutor)
                        if (libroModificado != null) {
                            println("Libro actualizado: $libroModificado")
                        } else {
                            println("No se pudo actualizar el libro.")
                        }
                    } else {
                        println("El libro con ID $idLibro y del autor con ID $idAutor no existe.")
                    }
                } else {
                    println("El autor con ID $idAutor no existe.")
                }
            }
            4 -> {
                // Lógica para eliminar un libro
                println("Ingrese el ID del autor del libro a eliminar:")
                val idAutor = readLine()?.toIntOrNull() ?: -1

                val autorExistente = autorRepo.getAutorByIdentificador(idAutor)
                if (autorExistente != null) {
                    println("Ingrese el ID del libro a eliminar:")
                    val idLibro = readLine()?.toIntOrNull() ?: -1

                    val eliminado = libroRepo.deleteByIdAndIdAutor(idAutor, idLibro)
                    if (eliminado) {
                        println("Libro con ID $idLibro eliminado correctamente.")
                    } else {
                        println("El libro con ID $idLibro y del autor con ID $idAutor no existe.")
                    }
                } else {
                    println("El autor con ID $idAutor no existe.")
                }
            }
            5 -> continuar = false
            else -> println("Opción no válida. Por favor, elige un número del menú.")
        }
    }
}

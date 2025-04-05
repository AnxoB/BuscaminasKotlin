data class Celda(var tieneMina: Boolean = false, var estaRevelada: Boolean = false, var tieneBandera: Boolean = false, var minasAdyacentes: Int = 0)

class Buscaminas(val filas: Int, val columnas: Int, val numMinas: Int) {
    val tablero: Array<Array<Celda>> = Array(filas) { Array(columnas) { Celda() } }
    var juegoTerminado: Boolean = false

    init {
        if (filas < 1 || columnas < 1) throw IllegalArgumentException("El número de filas y columnas debe ser mayor que 0")
        if (numMinas >= filas * columnas) throw IllegalArgumentException("El número de minas debe ser menor que el número de celdas del tablero")
        colocarMinas()
        contarMinasAdyacentes()
    }

    private fun colocarMinas() {
        var minasColocadas = 0
        while (minasColocadas < numMinas) {
            val fila = (0 until filas).random()
            val columna = (0 until columnas).random()
            if (!tablero[fila][columna].tieneMina) {
                tablero[fila][columna].tieneMina = true
                minasColocadas++
            }
        }
    }

    private fun contarMinasAdyacentes() {
        for (fila in 0 until filas) {
            for (columna in 0 until columnas) {
                if (!tablero[fila][columna].tieneMina) {
                    var minas = 0
                    for (i in -1..1) {
                        for (j in -1..1) {
                            val nuevaFila = fila + i
                            val nuevaColumna = columna + j
                            if (nuevaFila in 0 until filas && nuevaColumna in 0 until columnas && tablero[nuevaFila][nuevaColumna].tieneMina) {
                                minas++
                            }
                        }
                    }
                    tablero[fila][columna].minasAdyacentes = minas
                }
            }
        }
    }

    fun revelarCelda(fila: Int, columna: Int): Boolean {
        if (fila !in 0 until filas || columna !in 0 until columnas) throw IllegalArgumentException("Posición fuera del tablero")
        val celda = tablero[fila][columna]
        if (celda.estaRevelada || celda.tieneBandera) return false
        celda.estaRevelada = true
        if (celda.tieneMina) {
            juegoTerminado = true
            return true
        }
        if (celda.minasAdyacentes == 0) {
            for (i in -1..1) {
                for (j in -1..1) {
                    val nuevaFila = fila + i
                    val nuevaColumna = columna + j
                    if (nuevaFila in 0 until filas && nuevaColumna in 0 until columnas && !tablero[nuevaFila][nuevaColumna].estaRevelada) {
                        revelarCelda(nuevaFila, nuevaColumna)
                    }
                }
            }
        }
        return false
    }

    fun alternarBandera(fila: Int, columna: Int) {
        if (fila !in 0 until filas || columna !in 0 until columnas) throw IllegalArgumentException("Posición fuera del tablero")
        val celda = tablero[fila][columna]
        if (!celda.estaRevelada) {
            celda.tieneBandera = !celda.tieneBandera
        }
    }

    fun esJuegoTerminado(): Boolean = juegoTerminado

    fun imprimirTablero() {
        print("  ")
        for (col in 0 until columnas) {
            print("\u001B[34m$col \u001B[0m") // Blue color for column numbers
        }
        println()

        // Print rows with row numbers
        for (fila in 0 until filas) {
            print("\u001B[34m$fila \u001B[0m") // Blue color for row numbers
            for (columna in 0 until columnas) {
                val celda = tablero[fila][columna]
                print(
                    when {
                        celda.tieneBandera -> "B "
                        !celda.estaRevelada -> ". "
                        celda.tieneMina -> "* "
                        else -> "${celda.minasAdyacentes} " // Mostrar el número de minas adyacentes
                    }
                )
            }
            println()
        }
    }
}
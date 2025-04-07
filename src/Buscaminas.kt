class Buscaminas(val filas: Int, val columnas: Int, val numMinas: Int) {

    class Celda{
        private var _tieneMina = false
        private var _estaRevelada = false
        private var _tieneBandera = false
        private var _minasAdyacentes = 0

        val tieneMina: Boolean get() = _tieneMina
        val estaRevelada: Boolean get() = _estaRevelada
        val tieneBandera: Boolean get() = _tieneBandera
        val minasAdyacentes: Int get() = _minasAdyacentes

        fun ponerMina() {
            _tieneMina = true
        }

        fun revelar() {
            _estaRevelada = true
        }

        fun alternarBandera() {
            if (!_estaRevelada) {
                _tieneBandera = !_tieneBandera
            }
        }

        fun setMinasAdyacentes(cantidad: Int) {
            _minasAdyacentes = cantidad
        }
    }

    private val _tablero: Array<Array<Celda>> = Array(filas) { Array(columnas) { Celda() } }
    val tablero: Array<Array<Celda>>
        get() = _tablero.map { it.copyOf() }.toTypedArray()

    private var juegoTerminado: Boolean = false
    private var juegoGanado: Boolean = false

    fun inicializar() {
        colocarMinas()
        contarMinasAdyacentes()
    }

    private fun colocarMinas() {
        var minasColocadas = 0
        while (minasColocadas < numMinas) {
            val fila = (0 until filas).random()
            val columna = (0 until columnas).random()
            if (!tablero[fila][columna].tieneMina) {
                tablero[fila][columna].ponerMina()
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
                    tablero[fila][columna].setMinasAdyacentes(minas)
                }
            }
        }
    }

    fun revelarCelda(fila: Int, columna: Int): Boolean {
        val celda = tablero[fila][columna]
        if (celda.estaRevelada || celda.tieneBandera) return false
        celda.revelar()
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
        if (juegoGanado()) {
            juegoGanado = true
            juegoTerminado = true
        }
        return false
    }

    fun alternarBandera(fila: Int, columna: Int) {
        if (fila !in 0 until filas || columna !in 0 until columnas) throw IllegalArgumentException("Posición fuera del tablero")
        val celda = tablero[fila][columna]
        if (!celda.estaRevelada) {
            celda.alternarBandera()
        }
    }

    fun esJuegoTerminado(): Boolean = juegoTerminado

    fun juegoGanado(): Boolean {
        for (fila in tablero) {
            for (celda in fila) {
                if (!celda.tieneMina && !celda.estaRevelada) {
                    return false
                }
            }
        }
        return true
    }
}
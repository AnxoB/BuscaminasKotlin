fun main() {
    println("Bienvenido al juego de Buscaminas")
    println("Ingrese el número de filas del tablero:")
    val filas = readLine()!!.toInt()
    println("Ingrese el número de columnas del tablero:")
    val columnas = readLine()!!.toInt()
    println("Ingrese el número de minas:")
    val numMinas = readLine()!!.toInt()

    val juego = Buscaminas(filas, columnas, numMinas)
    if (filas < 1 || columnas < 1){
        throw IllegalArgumentException("El número de filas y columnas debe ser mayor que 0")
    }
    if (numMinas >= filas * columnas){
        throw IllegalArgumentException("El número de minas debe ser menor que el número de celdas del tablero")
    }

    while (!juego.esJuegoTerminado()) {
        imprimirTablero(juego.tablero)
        println("Ingrese la fila y columna para destapar (o 'b' para colocar/quitar bandera):")
        val entrada = readln()
        if (entrada=="b") {
            println("Ingresa la fila y columna donde quieres colocar la bandera")
            val bandera = readln().split(" ")
            val f = bandera[0].toInt()
            val c = bandera[1].toInt()
            juego.alternarBandera(f, c)
        } else {
            val (f, c) = entrada.trim().split(" ").map { it.toInt() }
            if (juego.revelarCelda(f, c)) {
                println("¡BOOM! Has pisado una mina. Fin del juego.")
            } else{
                if (juego.juegoGanado()){
                    println("Has ganado el juego")
                }
            }
        }
    }
    imprimirTablero(juego.tablero)


}

fun imprimirTablero(tablero: Array<Array<Buscaminas.Celda>>) {
    print("  ")
    for (col in tablero[0].indices) {
        print("\u001B[34m$col \u001B[0m")
    }
    println()

    for (fila in tablero.indices) {
        print("\u001B[34m$fila \u001B[0m")
        for (celda in tablero[fila]) {
            print(
                when {
                    celda.tieneBandera -> "B "
                    !celda.estaRevelada -> ". "
                    celda.tieneMina -> "* "
                    else -> "${celda.minasAdyacentes} "
                }
            )
        }
        println()
    }
}
fun main() {
    println("Bienvenido al juego de Buscaminas")
    println("Ingrese el número de filas del tablero:")
    val filas = readLine()!!.toInt()
    println("Ingrese el número de columnas del tablero:")
    val columnas = readLine()!!.toInt()
    println("Ingrese el número de minas:")
    val numMinas = readLine()!!.toInt()

    val juego = Buscaminas(filas, columnas, numMinas)

    while (!juego.esJuegoTerminado()) {
        juego.imprimirTablero()
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
            }
        }
    }
    println("Juego terminado.")
    juego.imprimirTablero()
}
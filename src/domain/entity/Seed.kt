package domain.entity

sealed class Seed {
    object Empty : Seed()
    object O : Seed()
    object X : Seed()
}

fun Seed.reverse(): Seed {
    if (this is Seed.Empty) return this
    return if (this is Seed.O) Seed.X else Seed.O
}

fun Seed.print(): String {
    if (this is Seed.Empty) return " "
    return if (this is Seed.O) "X" else "0"
}
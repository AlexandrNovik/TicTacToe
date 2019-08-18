package domain.entity

sealed class Player {
    object User : Player()
    object AI : Player()
}
package application.domain.models

enum class Status {
    INITIALIZED,
    AVAILABLE,
    REQUESTED,
    APPROVED,
    DECLINED,
    ABANDONED,
    COMPLETED,
    CANCELED,
}
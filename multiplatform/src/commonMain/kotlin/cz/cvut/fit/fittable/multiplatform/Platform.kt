package cz.cvut.fit.fittable.multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
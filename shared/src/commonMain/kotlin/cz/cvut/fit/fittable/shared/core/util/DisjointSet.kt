package cz.cvut.fit.fittable.shared.core.util

class DisjointSet<T> {
    private val parent = mutableMapOf<T, T>()

    fun find(event: T): T {
        if (!parent.containsKey(event)) {
            parent[event] = event
        }

        if (parent[event] != event) {
            parent[event] = find(parent[event]!!)
        }

        return parent[event]!!
    }

    fun union(event1: T, event2: T) {
        val root1 = find(event1)
        val root2 = find(event2)

        if (root1 != root2) {
            parent[root1] = root2
        }
    }
}
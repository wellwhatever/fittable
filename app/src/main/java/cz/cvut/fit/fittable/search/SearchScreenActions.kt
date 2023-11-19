package cz.cvut.fit.fittable.search

interface SearchScreenActions {
    fun onQueryChanged(query: String)
    fun onClearQueryClick()
}
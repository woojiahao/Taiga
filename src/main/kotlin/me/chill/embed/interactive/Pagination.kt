package me.chill.embed.interactive

class Pagination(
  private val total: Int,
  private val distribution: Map<Int, List<String>>
) {

  val hasMoreThanOnePage
    get() = total > 1

  var currentPage = 1
    private set

  val currentPageContent
    get() = distribution[currentPage] ?: emptyList()

  fun nextPage() {
    currentPage = if (currentPage == total) 1 else currentPage + 1
  }

  fun previousPage() {
    currentPage = if (currentPage == 1) total else currentPage - 1
  }

  override fun toString() = "Page $currentPage/$total"
}
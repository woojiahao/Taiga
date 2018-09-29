package me.chill.embed.interactive

class Pagination(
	private val total: Int,
	private val distribution: Map<Int, List<String>>
) {
	private var currentPage = 1

	fun nextPage() {
		currentPage = if (currentPage == total) 1 else currentPage + 1
	}

	fun previousPage() {
		currentPage = if (currentPage == 1) total else currentPage - 1
	}

	fun getCurrentPage() = distribution[currentPage]?.toTypedArray()

	fun hasMoreThanOnePage() = total > 1

	override fun toString() = "Page $currentPage/$total"
}
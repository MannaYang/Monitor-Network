package com.manna.monitor.report.markdown

class MarkdownBuilder {

    private val markdownBuilder = StringBuilder()

    fun title(level: Int,title: String): MarkdownBuilder {
        newLine()
        val prefix = when (level) {
            1 -> "#"
            2 -> "##"
            3 -> "###"
            4 -> "####"
            5 -> "#####"
            6 -> "######"
            else -> "######"
        }
        markdownBuilder.append("$prefix $title")
        return this
    }

    fun content(content: String): MarkdownBuilder {
        newLine()
        markdownBuilder.append(content)
        return this
    }

    fun color(color: String, content: String): MarkdownBuilder {
        markdownBuilder.append(" <font color=${color}>${content}</font> ")
        return this
    }

    fun quote(content: String): MarkdownBuilder {
        newLine()
        markdownBuilder.append("> $content")
        return this
    }

    fun newLine(): MarkdownBuilder {
        markdownBuilder.append("\n")
        return this
    }

    fun line(): MarkdownBuilder {
        newLine()
        markdownBuilder.append("---")
        newLine()
        return this
    }

    fun build(): String = markdownBuilder.toString()

    fun generate(): StringBuilder = markdownBuilder

    fun append(sb: StringBuilder): MarkdownBuilder {
        markdownBuilder.append(sb)
        return this
    }
}

package me.chill.commands.framework

// todo: make it so that the annotation takes in the category name, not the actual commands function
@Target(AnnotationTarget.FUNCTION)
annotation class CommandCategory
import com.android.build.gradle.LibraryExtension

apply<PublishPlugin>()

class PublishPlugin : Plugin<Project> {

    override fun apply(project: Project) = project.subprojects {
        afterEvaluate {
            if (projectDir.parentFile != rootDir
                    || !plugins.hasPlugin("maven-publish")) {
                return@afterEvaluate
            }

            publishing {
                publications {
                    register<MavenPublication>("release") {
                        groupId = GROUP_ID
                    }
                }
            }

            if (plugins.hasPlugin("com.android.library")) {
                android {
                    publishing {
                        singleVariant("release") {
                            withSourcesJar()
                            withJavadocJar()
                        }
                    }
                }
            }
        }
    }

    private fun Project.publishing(configure: PublishingExtension.() -> Unit) {
        extensions.configure("publishing", configure)
    }

    private fun Project.android(configure: LibraryExtension.() -> Unit) {
        extensions.configure("android", configure)
    }

    private companion object {
        const val GROUP_ID = "com.github.xiaocydxtest"
    }
}
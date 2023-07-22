import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId

object ProjectStub {
    fun get() = Project(
        id = ProjectId("03e13b55-b5b2-484d-a08b-b18aeb087c88"),
        title = "some title",
        description = "some description",
        createdBy = UserId("8098d197-a58f-4ae4-b602-8db6a146fb17"),
        permissions = mutableSetOf(ProjectPermission.READ, ProjectPermission.UPDATE, ProjectPermission.DELETE),
    )

    fun getList() = mutableListOf(
        Project(
            id = ProjectId("f89cc20d-bbf8-4fd8-9199-78d62d81209f"),
            title = "some project title 1",
            description = "some project description 1",
            createdBy = UserId("80d71f64-05b9-4901-a26b-69f35c3dc770"),
            permissions = mutableSetOf(ProjectPermission.READ, ProjectPermission.UPDATE),
        ),
        Project(
            id = ProjectId("afaf15b8-8c32-4c4d-b543-c18dfa1f8a15"),
            title = "some project title 2",
            description = "some project description 2",
            createdBy = UserId("caa9d872-ffb5-4cb8-93b8-d52dd9d36f68"),
            permissions = mutableSetOf(ProjectPermission.READ, ProjectPermission.DELETE),
        ),
    )

    fun prepareResult(block: Project.() -> Unit): Project = get().apply(block)

    fun prepareSearchList(searchText: String? = null, createdBy: UserId? = null) =
        listOf(
            prepareSearchItem(get(), "PRO-101", searchText, createdBy),
            prepareSearchItem(get(), "PRO-102", searchText, createdBy),
            prepareSearchItem(get(), "PRO-103", searchText, createdBy),
            prepareSearchItem(get(), "PRO-104", searchText, createdBy),
            prepareSearchItem(get(), "PRO-105", searchText, createdBy),
        )

    private fun prepareSearchItem(base: Project, newId: String?, searchText: String?, createdBy: UserId?): Project =
        base.copy(
            id = newId?.let { ProjectId(newId) } ?: base.id,
            title = searchText?.let { "${base.title} $searchText" } ?: base.title,
            description = searchText?.let { "${base.description} $searchText" } ?: base.description,
            createdBy = createdBy ?: base.createdBy,
        )
}

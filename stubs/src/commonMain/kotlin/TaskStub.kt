import me.neversleeps.common.models.task.Task
import me.neversleeps.common.models.task.TaskId
import me.neversleeps.common.models.task.TaskPermission
import me.neversleeps.common.models.task.TaskPriority
import me.neversleeps.common.models.task.TaskStatus
import me.neversleeps.common.models.task.TaskType
import me.neversleeps.common.models.user.UserId

object TaskStub {
    fun get() = Task(
        id = TaskId("1091efd3-ea95-4114-9117-fa007af4b4ec"),
        type = TaskType.BACKEND,
        priority = TaskPriority.HIGH,
        status = TaskStatus.TODO,
        title = "some title",
        description = "some description",
        executor = UserId("74ad94f2-f99f-4e6f-bd07-d0b0a2194849"),
        createdBy = UserId("6cc507ae-c4d5-4e6b-9870-54e672a75675"),
        permissions = mutableSetOf(TaskPermission.READ, TaskPermission.UPDATE, TaskPermission.DELETE),
    )

    fun getList() = mutableListOf(
        Task(
            id = TaskId("103fd31a-4e5d-407e-8713-45ac48b03cb4"),
            type = TaskType.DESIGN,
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.IN_PROGRESS,
            title = "some task title 1",
            description = "some task description 1",
            executor = UserId("0242fc00-c4c6-412e-b076-f2fa29403d61"),
            createdBy = UserId("7831d9f1-a4d2-40ea-9209-c97f3afa62d4"),
            permissions = mutableSetOf(TaskPermission.READ, TaskPermission.UPDATE),
        ),
        Task(
            id = TaskId("49454b0f-212a-44c9-b1ea-e365d592b0a7"),
            type = TaskType.DESIGN,
            priority = TaskPriority.MEDIUM,
            status = TaskStatus.DONE,
            title = "some task title 2",
            description = "some task description 2",
            executor = UserId("7a482f9a-025f-4b2e-bd3f-fe63aaf17011"),
            createdBy = UserId("f8eaae46-36bc-41ef-9cce-aede11d2a47a"),
            permissions = mutableSetOf(TaskPermission.READ, TaskPermission.DELETE),
        ),
    )
}

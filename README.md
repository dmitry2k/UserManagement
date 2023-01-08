# DropWizard User Management Service

We want you to build a new user management service. The service must offer CRUD operations for the users. 
In order to achieve this, we want you to build a new service that exposes an API with the following specifications:

### 1. Create a new user (POST /users route)

Request model must include email, first name, last name fields. All fields should be validated. Email field must contain valid email. First name and last name must not exceed 100 characters.

Response model must include id, email, first name, last name, created at, deleted at fields. Let's call this model `User`.

If email field from request has been taken by another user (even if this user has been deleted), 409 error code (CONFLICT) should be thrown in response.

### 2. Read user by id (GET /users/{user-id} route)

Response model is the `User` model from create a new user route.

If user has not been found, 404 error code (NOT_FOUND) should be thrown in response. Deleted users should be shown by this route.

### 3. List users (GET /users route)

This route can include some/all of these query params:
1. limit - maximum number of users in the response (min value - 1, max value - 100, default - 25)
2. offset - number of users to skip before putting in the response (min value - 0, default - 0)
3. sort - Sort the users by the given conditions, in order. It must accept only one of `id`, `email`, `first_name`, `last_name`, `created_at`, `deleted_at` values. Default is `id` value.
4. order - Direction in which the resulting list is sorted. It must accept only `ASC` or `DESC`. Default is `ASC` value.
5. show_deleted - whether or not to return deleted users with this request. Default is `false` value.

Response model is the list of `User`.

### 4. Update user by id (PUT /users/{id} route)

Request model includes only first name and last name fields. All fields are nullable. All non-null fields must be validated. We need to update only non-null fields.

Response model is `User` model.

If user has not been found or has been already deleted, 404 error code (NOT_FOUND) should be thrown in response.

### 5. DELETE user by id (DELETE /users/{id} route)

We use soft-deletion approach. It means we don't delete the database row related to this user, but we fill deleted at field with current timestamp.

Response model is `User` model.

If user has not been found or has been already deleted, 404 error code (NOT_FOUND) should be thrown in response.

### Other requirements:

1. You need to create repository in GitHub or GitLab.
2. You need to make Swagger UI available and describe resource, routes, path and query params using Swagger annotations ([how to enable Swagger UI instruction](https://github.com/smoketurner/dropwizard-swagger#how-to-use-it) and [Swagger annotations description](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X))
3. Use JDBI3's annotation's approach to interact with database (https://jdbi.org/)
4. Use Jackson annotation for web model serialization/deserialization
5. Try to use Kodein as IoC container (https://habr.com/ru/post/431696/)
6. Try to use annotations for web models validation (https://www.dropwizard.io/en/latest/manual/validation.html)
7. Code must be covered by tests. Please, use dropwizard test article as an example: https://www.dropwizard.io/en/latest/manual/testing.html


### Some notes:

First of all you need to start MySQL docker container:
`gradle startMysqlDevContainer`

To start server you can run this command:
`gradle run --args "server config.yaml"`

After stopping server, you can stop MySQL docker container:
`gradle stopMysqlDevContainer`
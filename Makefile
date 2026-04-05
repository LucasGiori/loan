
build:
	"./gradlew build"

test:
	docker exec loan-app ./gradlew test --tests "application.domain.*"
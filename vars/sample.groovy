def call(body) {

    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node {
	stage('stage-1'){
		echo "Hello ${config.name}"
	}
    }
}

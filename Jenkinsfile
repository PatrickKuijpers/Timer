#!groovy

node {
    def BRANCH_NAME = env.BRANCH_NAME
    echo "Using branch: ${BRANCH_NAME}"

    def PARAMS = [string(name: 'BRANCH_NAME', value: BRANCH_NAME)]

    stage('Unittests') {
        build job: 'Timer, Unittests', parameters: PARAMS
    }
}

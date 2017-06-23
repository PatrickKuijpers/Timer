#!groovy

node {
    def AUTODEPLOY_TURNED_ON = false // TODO: Auto-deploy is nog niet volledig geconfigureerd

    def BRANCH_NAME = env.BRANCH_NAME
    echo "Using branch: ${BRANCH_NAME}"

    def PARAMS = [string(name: 'BRANCH_NAME', value: BRANCH_NAME)]

    stage('Unittests') {
        build job: 'Timer, Unittests', parameters: PARAMS
    }

    if (BRANCH_NAME == 'master') {
        stage('Release to Google Play Store (beta-test)') { // TODO: Voorlopig uploaden naar beta-test ipv direct live
            if (AUTODEPLOY_TURNED_ON) {
                build job: 'Timer, Google Play Store, release', parameters: PARAMS
            } else {
                echo "The app can be released now!"
                echo "However, auto-deploy is not turned on..."

                //TODO: tijdelijk ook master branch naar HockeyApp releasen:
                echo "Temporarily release master to HockeyApp!"
                stage('Upload to HockeyApp (alpha-test)') {
                    build job: 'Timer, HockeyApp, alpha', parameters: PARAMS
                }
            }
        }
    } else if (BRANCH_NAME.contains('release/') || BRANCH_NAME.contains('hotfix/')) {
        stage('Upload to HockeyApp (alpha-test)') {
            build job: 'Timer, HockeyApp, alpha', parameters: PARAMS
        }
    } else { // Develop & alle feature branches
    }
}

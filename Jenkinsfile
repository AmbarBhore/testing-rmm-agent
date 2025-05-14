pipeline {
	agent any
	environment {
		DOCKER_IMAGE = 'ambarbhore1234/rmmagent'
		KUBECONFIG_CRED_ID = 'kubeconfig'
	}
	
	stages {
		stage('clone repo') {
			steps {
				git branch: 'main', credentialsId: 'github-config', url: 'https://github.com/AmbarBhore/RMM-Agent-3.1.git'			}
		}
		stage('Build') {
			steps {
				sh 'mvn clean package'
			}
		}
		stage('Image push to docker-hub') {
			steps {
				withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
					sh '''
					   DOCKER_TAG=$BUILD_NUMBER
					   echo "Building an docker image : $DOCKER_IMAGE"
					   docker build	-t $DOCKER_IMAGE:$DOCKER_TAG .
						
					   echo "Logging into the Docker hub"
				           echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin						
					   echo "pushing image to the docker hub and updating the tag as well"
					   docker push $DOCKER_IMAGE:$DOCKER_TAG
				        '''
				}
			}
		}
		stage('Deploy to kubernetes') {
                        steps {
                                withCredentials([file(credentialsId: "${KUBECONFIG_CRED_ID}", variable: 'kubeconfig')]) {
                                        sh '''
					    export KUBECONFIG=$kubeconfig
						
					    kubectl config current-context
					    kubectl get nodes
						
                                            echo "Applying deployment and service"
                                            kubectl apply -f k8s/deployment.yaml
                                            kubectl apply -f k8s/service.yaml

                                            echo "Updating image with the build tag: $BUILD_NUMBER"
                                            kubectl set image deployment/rmm-agent rmm-agent=$DOCKER_IMAGE:$BUILD_NUMBER --record
					'''
				}
			}
		}
	}
}

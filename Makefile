default:
	@echo "no default target for Makefile"
	exit 1

clean:
	mvn clean
	
fmt:
	mvn process-sources -P format

test:
	mvn test -Dmaven.test.skip=false

build:
	mvn package

verify:
	mvn verify -P oss

deploy:
	mvn clean deploy -P oss -DautoReleaseAfterClose=true

version:
	mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$(version)

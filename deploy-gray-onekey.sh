cd /opt/projects/suntech/src/suntech
BRANCH=develop
while getopts "B:" opt; do
  case ${opt} in
    B)
      BRANCH="$OPTARG";
      ;;
    \?)
      echo "Invalid option: -$OPTARG";
      exit 1;
      ;;
  esac
done
git checkout ${BRANCH}

git pull
mvn clean compile package -DskipTests=true -Pgray
./build.sh
docker-compose down
docker-compose up -d

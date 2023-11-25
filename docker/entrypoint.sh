#!/usr/bin/env sh
# $0 is a script name,
# $1, $2, $3 etc are passed arguments
# $1 is our command
CMD=$1
TYPE=$2

SECRETS_DIR=/vault/secrets

if [ -d ${SECRETS_DIR} ];
then
  # obtain secrets passed as environment variables e.g. passwords
	echo "[INFO] Processing secrets (env)..."
  for SECRET_FILE in `find ${SECRETS_DIR} -name "*.env"`; do
    . ${SECRET_FILE}
  done
  # obtain base64-encoded secrets e.g. keystores
	echo "[INFO] Processing secrets (b64)..."
  for SECRET_FILE in `find ${SECRETS_DIR} -name "*.b64"`; do
    FILENAME="${SECRET_FILE##*/}"
    echo "[INFO] Decoding [${SECRET_FILE}] into [${FILENAME}]"
    base64 -d ${SECRET_FILE} > /vault/secrets/${FILENAME%.[^.]*}
  done
else
  echo "[WARN] No secrets injected from Vault"
fi

echo "[INFO] Starting application"
case "$CMD" in
  "run" )

    exec java $JAVA_OPTS -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar /dps/app.jar
    ;;

   * )
    # Run custom command. Thanks to this line we can still use
    # "docker run our_image /bin/bash" and it will work
    exec $CMD ${@:2}
    ;;
esac
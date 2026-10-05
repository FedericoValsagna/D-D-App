FROM docker:29-cli
RUN apk add --no-cache bash git curl jq
COPY deploy.sh /usr/local/bin/deploy.sh
ENTRYPOINT ["/usr/local/bin/deploy.sh"]

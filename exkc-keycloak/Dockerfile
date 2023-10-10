FROM quay.io/keycloak/keycloak:21.1.1 AS keycloak-build

ENV KC_DB=postgres
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_FEATURES=token-exchange,admin2

COPY realm/exkc.json /opt/keycloak/data/import/realm.json

ENTRYPOINT [ "/opt/keycloak/bin/kc.sh" ]

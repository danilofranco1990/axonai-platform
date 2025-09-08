#!/bin/sh
# Habilita a saída imediata em caso de erro
set -e

echo "--- DIAGNÓSTICO DAS VARIÁVEIS DE AMBIENTE ---"
echo "SPRING_DATASOURCE_URL=[$SPRING_DATASOURCE_URL]"
echo "SPRING_DATASOURCE_USERNAME=[$SPRING_DATASOURCE_USERNAME]"
echo "--- FIM DO DIAGNÓSTICO ---"

# O comando 'exec' substitui o processo do shell pelo processo Java,
# o que é a maneira correta de iniciar a aplicação principal.
exec java -jar app.jar
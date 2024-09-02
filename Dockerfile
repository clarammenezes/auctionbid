# Use uma imagem base do OpenJDK
FROM eclipse-temurin:17.0.12_7-jre-jammy

# Defina o diretório de trabalho
WORKDIR /app

# Copie o JAR para o contêiner e renomeie se necessário
COPY target/auction.bid-0.0.1-SNAPSHOT.jar /app/auctionbid.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "./auctionbid.jar"]


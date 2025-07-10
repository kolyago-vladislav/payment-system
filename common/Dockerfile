FROM gradle:8.14.0-jdk24
WORKDIR /app
COPY --chown=gradle:gradle . .
CMD ["gradle", "clean", "build", "publish", "--no-daemon"]
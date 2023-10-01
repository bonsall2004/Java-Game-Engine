#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 fragTextureCoord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 0.5);
    fragTextureCoord = textureCoords;
}

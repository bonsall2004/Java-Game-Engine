#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec3 colour;

void main() {
    gl_Position = vec4(position, 1);
    colour = vec3(position.x + 0.25, 0.17, position.y + 0.25);
}
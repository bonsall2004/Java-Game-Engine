#version 330 core

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColour;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight {
    vec3 colour;
    vec3 direction;
    float intensity;
};

uniform sampler2D textureSampler;
uniform Material material;
uniform vec3 ambientLight;
uniform float specularPower;
uniform DirectionalLight directionalLight;

void main() {
    vec4 ambientC;
    vec4 diffuseC;
    vec4 specularC;

    if (material.hasTexture == 1) {
        ambientC = texture(textureSampler, fragTextureCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }

    vec3 toLightDir = normalize(directionalLight.direction);
    vec3 normal = normalize(fragNormal);
    vec3 viewDir = normalize(-fragPos);

    float diffuseFactor = max(dot(normal, toLightDir), 0.0);
    vec4 diffuseColor = diffuseC * vec4(directionalLight.colour, 1.0) * directionalLight.intensity * diffuseFactor;

    vec3 halfwayDir = normalize(toLightDir + viewDir);
    float specularFactor = pow(max(dot(normal, halfwayDir), 0.0), specularPower);
    vec4 specularColor = specularC * vec4(directionalLight.colour, 1.0) * directionalLight.intensity * specularFactor * material.reflectance;

    vec4 finalColor = (ambientC + diffuseColor + specularColor) * vec4(ambientLight, 1.0);

    fragColour = finalColor;
}

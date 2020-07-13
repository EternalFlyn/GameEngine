#version 450 core

layout (location = 0) in vec2 coords;
layout (location = 1) in vec3 surfaceNormal;
layout (location = 2) in vec3 toLightVector;
layout (location = 3) in vec3 toCameraVector;

layout (location = 0) out vec4 pixelColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float minBrightness;

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float brightness = max(dot(unitNormal, unitLightVector), minBrightness);
	vec4 diffuse = vec4(brightness * lightColor, 1);
	
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
	float specularFactor = max(dot(unitCameraVector, reflectedLightDirection), 0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec4 finalSpecular = vec4(dampedFactor * reflectivity * lightColor, 1);
	
	vec4 textureColor = texture(textureSampler, coords);
	if(textureColor.a < 0.5) {
		discard;
	}
	
	pixelColor = diffuse * textureColor + finalSpecular;
}
#version 450 core

layout (location = 0) in vec2 coords;
layout (location = 1) in vec3 surfaceNormal;
layout (location = 2) in vec3 toLightVector;
layout (location = 3) in vec3 toCameraVector;
layout (location = 4) in float visibility;

layout (location = 0) out vec4 pixelColor;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform vec3 skyColor;
uniform vec3 grassColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float minBrightness;

void main(void) {

	vec4 blendMapColor = texture(blendMap, coords * 4);
	vec2 tiledCoords = coords * 4096;
	float backgroundAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec4 backgroundColor = vec4(grassColor, 1) * texture(backgroundTexture, tiledCoords) * backgroundAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;
	vec4 finalTextureColor = backgroundColor + rTextureColor + gTextureColor + bTextureColor;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float brightness = max(dot(unitNormal, unitLightVector), minBrightness);
	vec4 diffuse = vec4(brightness * lightColor, 1);
	
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
	float specularFactor = max(dot(unitCameraVector, reflectedLightDirection), 0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec4 finalSpecular = vec4(dampedFactor * reflectivity * lightColor, 1);
	
	pixelColor = diffuse * finalTextureColor + finalSpecular;
	pixelColor = mix(vec4(skyColor, 1), pixelColor, visibility);
}
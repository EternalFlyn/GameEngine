#version 450 core

in vec2 coords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 pixelColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform vec3 skyColor;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

	vec4 textureColor = texture(textureSampler, coords);
	if(textureColor.a < 0.5) {
		discard;
	}

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitCameraVector = normalize(toCameraVector);
	
	vec3 diffuse = vec3(0);
	vec3 specular = vec3(0);
	
	for(int i = 0; i < 4; i++) {
		vec3 unitLightVector = normalize(toLightVector[i]);
		float lightDistance = length(toLightVector[i]);
		
		float attenuationFactor = attenuation[i].x + attenuation[i].y * lightDistance + attenuation[i].z * lightDistance * lightDistance;
		float brightness = max(dot(unitNormal, unitLightVector), 0);
		vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
		float specularFactor = max(dot(unitCameraVector, reflectedLightDirection), 0);
		float dampedFactor = pow(specularFactor, shineDamper);
		
		diffuse += (brightness / attenuationFactor) * lightColor[i];
		specular += (dampedFactor * reflectivity / attenuationFactor) * lightColor[i];
	}
	diffuse = max(diffuse, 0.2);
	
	pixelColor = vec4(diffuse, 1) * textureColor + vec4(specular, 1);
	pixelColor = mix(vec4(skyColor, 1), pixelColor, visibility);
}
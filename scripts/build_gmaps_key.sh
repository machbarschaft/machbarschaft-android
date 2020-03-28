#!/bin/bash
# Creates the google maps api key resource file.
# Adds the key provided via the environment variable GMAPS_API_KEY.

mkdir -p app/src/debug/res/values
cat <<EOF > app/src/debug/res/values/google_maps_api.xml
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">$GMAPS_API_KEY</string>
</resources>
EOF
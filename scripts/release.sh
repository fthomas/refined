#!/bin/sh

OLD_VERSION=$(git describe --abbrev=0 --tags | tr -d 'v\n')

VERSION="$1"
if [ -z "$VERSION" ]; then
    exit 1
fi

NOTES="notes/$VERSION.markdown"
echo "Add release date to $NOTES"
NOW=$(date +%Y-%m-%d)
echo "\nReleased on $NOW" >> "$NOTES"
git add "$NOTES"

README="README.md"
echo "Replace $OLD_VERSION with $VERSION in $README"
sed -i -e "s/$OLD_VERSION/$VERSION/" "$README"
git add "$README"

git commit -a -m "Setting version to $VERSION"
git tag -a -s v$VERSION -m "Releasing $VERSION"

LATEST_VERSION_SBT="latestVersion.sbt"
echo "Update $LATEST_VERSION_SBT"
LATEST_VERSION_DEF="latestVersion in ThisBuild :="
sed -i -e "s/$LATEST_VERSION_DEF \"$OLD_VERSION\"/$LATEST_VERSION_DEF \"$VERSION\"/" \
  "$LATEST_VERSION_SBT"
sed -i -e "s/\/\/ NEXT_VERSION/, \"$VERSION\"\n  \/\/ NEXT_VERSION/" "$LATEST_VERSION_SBT"
sbt scalafmtSbt
git add "$LATEST_VERSION_SBT"

git commit -a -m "Set $VERSION as latestVersion"

#git push
#git push --tags

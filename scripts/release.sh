#!/bin/sh

OLD_VERSION=$(git describe --abbrev=0 --tags | tr -d 'v\n')
if [ -n "$2" ]; then
    OLD_VERSION="$2"
fi

VERSION="$1"
if [ -z "$VERSION" ]; then
    exit 1
fi

README="README.md"
echo "Replace $OLD_VERSION with $VERSION in $README"
sed -i -e "s/$OLD_VERSION/$VERSION/g" "$README"
git add "$README"

git commit -a -m "Setting version to $VERSION"
git tag -a -s "v$VERSION" -m "Releasing $VERSION"

LATEST_VERSION_SBT="latestVersion.sbt"
echo "Update $LATEST_VERSION_SBT"
LATEST_VERSION_DEF="ThisBuild \\/ latestVersion :="
sed -i -e "s/$LATEST_VERSION_DEF \"$OLD_VERSION\"/$LATEST_VERSION_DEF \"$VERSION\"/" \
  "$LATEST_VERSION_SBT"
sed -i -e "s/\/\/ NEXT_VERSION/, \"$VERSION\"\n  \/\/ NEXT_VERSION/" "$LATEST_VERSION_SBT"
sbt scalafmtSbt
git add "$LATEST_VERSION_SBT"

git commit -a -m "Set $VERSION as latestVersion"

#git push
#git push --tags
